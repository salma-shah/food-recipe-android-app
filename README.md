

# FoodBunny - Android Mobile Application

## About

This project is an application that allows users to view and scroll through different food recipes, search by name, and filter recipes by type and category (dairy, vegan, overnight/breakfast, dinner, desserts). Users can favourite recipes, leave reviews, and upload their own recipes. Users can manage their profile details, and modify or delete their recipes. 

Additionally, the FoodBunny ChatBot is a feature of this app. Users can interact with the FoodBunny ChatBot, which is connected to an AI/ML API; this way, an AI model is integrated into this application by using an API key.

FoodBunny has many features that assist users in their day-to-day life; it solves an issue many of us face when it comes to tackling food recipes —finding recipes that can be made with minimal effort and the available ingredients. FoodBunny is meant to serve as a practical solution to users. 

## Built With
* Java
* XML
* SQLite Relational Database
* AIMLAPI API key
* Android Studio
* Figma


This was built on Android Studio and can be run on either an emulator or connected to an Android mobile device.

The front-end of the application was built using XML elements, icons, and images, while the backend was built using Java. The database was a SQLite relational database available in the Android Studio environment, and a DBHelper class was created to manage database management related functions. These functions were later called in the Java classes when necessary. 

For the FoodBunny ChatBot, an AIMLAPI API key was used. An OpenAIHelper was created to write the code linking the application with the key; the key was then pasted in the ChatBot fragment, where the OpenAIHelper class was called. 

## Lessons Learned

* This was my first personal project, and I created it to solve a problem that most of us deal with: struggling with deciding on which recipe to cook, with whatever available ingredients we have on hand. It also gives users a chance to share their own recipes, and modify it according to whatever changes they might have made to produce a better result. 
* Integrating the AI FoodBunny ChatBot was challenging since OpenAI required a paid verification. So I decided to use AI/ML instead, which is free, and integrated the AI model into the FoodBunny application through a generated API key. 
* This project gave me the chance to improve and polish my Java skills, after my CityCycle Rental app. I learnt to improve my XML layout and add new features, including adding/removing favourites, deleting functions, and logging out.




