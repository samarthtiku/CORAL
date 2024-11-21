# CORAL: Carbon Observation, Reduction, and Automated Lifestyle-tracker

## Vision
CORAL is an end-to-end mobile application that helps users understand and reduce their personal carbon footprint, mobilizing global citizens to realize their potential in the fight against climate change. Inspired by the UN's Goal 13 (Sustainable Development), CORAL aims to launch at Loyola University Chicago and scale across Illinois, the US, and eventually over 24 countries, fostering international collaboration for global climate action.

## Problem Statement
Loyola University Chicago has made strides towards carbon neutrality, yet significant challenges remain. There is currently no clear way to monitor and manage personal carbon emissions. This gap limits our sustainability efforts and hinders our ability to effectively reduce climate change impacts. CORAL seeks to fill this void by providing a definitive application that measures carbon footprints and encourages actionable steps to reduce them.

## Project Description, Goals, and Deliverables
CORAL will deliver an all-in-one mobile app designed to:
1. Track and manage personal carbon footprints through automated and manual input.
2. Offer tailored suggestions to reduce carbon emissions based on personalized data.
3. Engage users with interactive data visualizations.

The project will also develop a powerful API for integration with third-party platforms, applications, and IoT devices, enhancing the app's impact and reach.

## Target Users
- Individuals aged 8 and older
- Students and employees of Loyola University Chicago
- Educational institutions, corporations, offices, government agencies, NGOs
- Eco-friendly startups and community organizations

## Tools and Technologies
- **Backend Development**: Java, JDK 17 + Spring Boot 2.5
- **Android UI/UX Development**: Kotlin (Android Studio)
- **Database**: SQLite / PostgreSQL
- **Build Tool**: Gradle 8.5
- **APIs**: RESTful APIs, Google API or other internal RESTful APIs
- **Data Visualization**: D3.js
- **Testing**: Junit 5, Minimal UI testing with Espresso
- **Version Control**: GitHub, GitHub Actions
- **Documentation**: Swagger v2 3.0

## Layered Architecture
CORAL's architecture is designed to be flexible, featuring layers for:
- User Interface
- Logic Processing
- Carbon Analytics
- Data Management
- External Connectivity
- Additional specific modules

## Architectural Decisions
- As we all have different experiences with Java & Kotlin, we split the project 2 ways.
- We first built out the project using JavaSwing as a proof-of-concept. 
- We are currently developing the full version in Intellij using Kotlin. The logic as well as a lot of the code from the proof of concept can be used in the full version.
