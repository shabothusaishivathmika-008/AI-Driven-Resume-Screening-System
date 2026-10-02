# AI-Driven Resume Screening System

An AI-driven resume screening system developed in Java to automate resume parsing, candidate scoring, ranking, and recruiter-oriented screening.

## Project Overview

Manual resume screening can be time-consuming and inconsistent when recruiters need to evaluate a large number of resumes.

This project implements an automated resume screening pipeline that processes resumes against a job description and produces ranked candidates.

The system follows this basic flow:

Job Description
        ↓
Resume Files
        ↓
Text Extraction
        ↓
Text Processing
        ↓
TF-IDF
        ↓
Cosine Similarity
        ↓
Rule-Based Scoring
        ↓
Combined Score
        ↓
Candidate Ranking
        ↓
Recruiter Output

## Problem Statement

The system is designed to address common problems in manual resume screening:

- Time-consuming resume evaluation
- Inconsistent shortlisting
- Keyword-based bias
- Lack of systematic skill scoring
- Lack of an automated ranking mechanism

## Objectives

The main objectives of the project are:

1. Parse resume information automatically.
2. Extract candidate skills, experience, education, and other text.
3. Compare resumes with a job description.
4. Calculate text similarity using TF-IDF and cosine similarity.
5. Calculate rule-based scores for skills, experience, and education.
6. Combine the scores into an overall candidate score.
7. Rank candidates according to their scores.
8. Support multithreaded resume screening.
9. Cache screening results for repeated searches.
10. Track candidate shortlist popularity.

## Technologies Used

- Java
- JDK 17+
- Visual Studio Code
- Java File I/O
- Java Collections Framework
- HashMap
- PriorityQueue
- TF-IDF
- Cosine Similarity
- Regular Expressions
- Multithreading
- ExecutorService
- Result Caching

## Project Structure

```text
AIResumeScreening
│
├── src
│   ├── Main.java
│   ├── Resume.java
│   ├── JobDescription.java
│   ├── Candidate.java
│   ├── TextParser.java
│   ├── TFIDFCalculator.java
│   ├── ScoringEngine.java
│   ├── RankingEngine.java
│   ├── ScreeningSystem.java
│   ├── MultithreadedScreening.java
│   ├── ResultCache.java
│   ├── PopularityTracker.java
│   └── TestRunner.java
│
├── data
│   ├── jobdesc.txt
│   └── resumes
│       ├── resume_01.txt
│       ├── resume_02.txt
│       ├── resume_03.txt
│       ├── resume_04.txt
│       ├── resume_05.txt
│       ├── resume_06.txt
│       ├── resume_07.txt
│       └── resume_08.txt
│
├── test
│
└── README.md