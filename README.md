Overview

This Android application, built using Kotlin, demonstrates how to retrieve and display data from multiple public web APIs. The app allows users to fetch a random quote and then perform further API requests based on the retrieved data. Users can either view more quotes from the same author or search for advice related to a keyword from the initial quote.

Features

•	Retrieves Data from Multiple APIs: The application makes network requests to fetch quotes and advice using different APIs.

•	Dynamic API Requests: A subsequent API request is made based on data retrieved from the first request.

•	Lists of Data Displayed: Retrieved quotes and advice lists are displayed using a RecyclerView.

•	Custom Data Model: A custom class represents JSON objects retrieved from the APIs.

•	Fragments for UI Management: The app uses fragments to display the lists of quotes and advice.

•	Styled UI Components: RecyclerView items have rounded corners for an improved visual experience.

•	Error Handling: Users receive notifications if API requests fail or time out.

•	User Interaction: The user selects when to make an API request and can choose between different actions.


APIs Used

•	Quote API: Provides a random quote with author details.

•	Advice API: Retrieves a list of advice containing a specific keyword from the quote.

How It Works

1.	The user requests a random quote.
2.	The retrieved quote is displayed along with options:
o	View more quotes from the same author.
o	Search for advice containing the first word of the quote.
3.	Based on the user’s choice, another API request is made, and the data is displayed in a list format.
Technologies Used

•	Kotlin for Android development

•	RecyclerView for displaying lists

•	Fragments for UI management

•	View Binding for referencing views

•	Networking Library (such as Volley) for API requests

Setup Instructions

1.	Clone the repository:
git clone https://github.com/AdrianSavaUoC/AndroidWebAPI.git
2.	Open the project in Android Studio.
3.	Ensure you have an active internet connection for API requests.
4.	Run the app on an Android Emulator (Pixel 3a, API 34) or a physical device.
   

Developer: Adrian Sava

