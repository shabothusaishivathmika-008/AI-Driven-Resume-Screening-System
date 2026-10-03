const screenButton =
    document.getElementById("screenButton");

const candidateResults =
    document.getElementById("candidateResults");

const resultStatus =
    document.getElementById("resultStatus");

const candidateProfile =
    document.getElementById("candidateProfile");

const closeProfile =
    document.getElementById("closeProfile");


/*
 * ==========================================
 * SCREEN RESUMES USING JAVA BACKEND
 * ==========================================
 */

screenButton.addEventListener(
    "click",
    async function () {

        // Show loading message

        resultStatus.textContent =
            "Screening resumes...";

        resultStatus.style.background =
            "#fef3c7";

        resultStatus.style.color =
            "#92400e";

        candidateResults.innerHTML = `
            <div class="empty-state">
                <h3>Processing resumes...</h3>
                <p>
                    Java screening engine is analyzing
                    the candidates.
                </p>
            </div>
        `;


        try {

            /*
             * Call Java backend API
             */

            const response =
                await fetch("/api/screen");


            /*
             * Check whether request succeeded
             */

            if (!response.ok) {

                throw new Error(
                    "Server returned status "
                    + response.status
                );
            }


            /*
             * Convert JSON response
             * into JavaScript data
             */

            const candidates =
                await response.json();


            /*
             * Check for backend error
             */

            if (candidates.error) {

                throw new Error(
                    candidates.error
                );
            }


            /*
             * Screening successful
             */

            resultStatus.textContent =
                "Screening completed";

            resultStatus.style.background =
                "#dcfce7";

            resultStatus.style.color =
                "#166534";


            /*
             * Remove old results
             */

            candidateResults.innerHTML = "";


            /*
             * If no candidates were returned
             */

            if (candidates.length === 0) {

                candidateResults.innerHTML = `
                    <div class="empty-state">
                        <h3>No candidates found</h3>
                        <p>
                            No valid resumes were available
                            for screening.
                        </p>
                    </div>
                `;

                return;
            }


            /*
             * Display every candidate
             */

            candidates.forEach(
                function (candidate) {

                    const row =
                        document.createElement("div");

                    row.className =
                        "candidate-row";


                    row.innerHTML = `

                        <div class="rank">
                            #${candidate.rank}
                        </div>


                        <div>

                            <div class="candidate-name">
                                ${candidate.name}
                            </div>

                            <span class="candidate-id">
                                ${candidate.id}
                            </span>

                        </div>


                        <div>

                            <div class="candidate-skills">
                                ${candidate.skills.join(", ")}
                            </div>

                            <div class="candidate-skills">
                                ${candidate.experience}
                                years
                            </div>

                        </div>


                        <div class="score">
                            ${candidate.score.toFixed(2)}
                        </div>

                    `;


                    /*
                     * Click candidate
                     * to open profile
                     */

                    row.addEventListener(
                        "click",
                        function () {

                            showCandidateProfile(
                                candidate
                            );

                        }
                    );


                    candidateResults.appendChild(
                        row
                    );

                }
            );

        }

        catch (error) {

            /*
             * Display error
             */

            console.error(
                "Screening error:",
                error
            );


            resultStatus.textContent =
                "Screening failed";

            resultStatus.style.background =
                "#fee2e2";

            resultStatus.style.color =
                "#991b1b";


            candidateResults.innerHTML = `

                <div class="empty-state">

                    <h3>
                        Unable to complete screening
                    </h3>

                    <p>
                        ${error.message}
                    </p>

                    <p>
                        Make sure the Java server
                        is running.
                    </p>

                </div>

            `;

        }

    }
);


/*
 * ==========================================
 * SHOW CANDIDATE PROFILE
 * ==========================================
 */

function showCandidateProfile(candidate) {


    /*
     * Basic information
     */

    document.getElementById(
        "profileName"
    ).textContent =
        candidate.name;


    document.getElementById(
        "profileId"
    ).textContent =
        candidate.id;


    document.getElementById(
        "profileExperience"
    ).textContent =
        candidate.experience + " years";


    document.getElementById(
        "profileEducation"
    ).textContent =
        candidate.education;


    /*
     * Skills
     */

    const skillsContainer =
        document.getElementById(
            "profileSkills"
        );


    skillsContainer.innerHTML = "";


    candidate.skills.forEach(
        function (skill) {

            const skillElement =
                document.createElement("span");

            skillElement.className =
                "profile-skill";

            skillElement.textContent =
                skill;

            skillsContainer.appendChild(
                skillElement
            );

        }
    );


    /*
     * Score breakdown
     */

    document.getElementById(
        "profileCosine"
    ).textContent =
        candidate.cosine.toFixed(2);


    document.getElementById(
        "profileSkillScore"
    ).textContent =
        candidate.skillScore.toFixed(2);


    document.getElementById(
        "profileExperienceScore"
    ).textContent =
        candidate.experienceScore.toFixed(2);


    document.getElementById(
        "profileEducationScore"
    ).textContent =
        candidate.educationScore.toFixed(2);


    document.getElementById(
        "profileTotalScore"
    ).textContent =
        candidate.score.toFixed(2);


    /*
     * Show profile panel
     */

    candidateProfile.classList.remove(
        "hidden"
    );


    /*
     * Smoothly scroll to profile
     */

    candidateProfile.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });

}


/*
 * ==========================================
 * CLOSE PROFILE
 * ==========================================
 */

closeProfile.addEventListener(
    "click",
    function () {

        candidateProfile.classList.add(
            "hidden"
        );

    }
);