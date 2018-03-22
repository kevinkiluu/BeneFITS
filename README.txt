BeneFITS Application
Tested with the Samsung Galaxy S7 on Android Nougat

MAIN SOURCE CODE (\app\src\main\java\com\example\ddk\benefits\):
DataParser - Google Maps API Data Parser Helper Class used in What's Nearby page. Returns and processes Google Places based on category user selects on What's Nearby page.

DiaryFragment - Fragment of MainActivity used for the Log Food Diary page. Contains a search query engine that returns a ranked list of relevant results from a web API call to the USDA Food Composition Database based on the query input.

DownloadURL - Helper class used to fetch URLs for web API calls.

FoodFragment - Accessed from the DiaryFragment part of the Log Food Diary page. Upon click of a query item, uses a second web API call to the USDA Food Composition Database to fetch specific nutritional facts about the food clicked.

GetNearbyPlacesData - Google Maps API Helper Class used in What's Nearby Page. Synergizes with DataParser to fetch places from the Google Places API.

HomeFragment - The home page presented upon login of BeneFITS. Shows an overview of calories remaining as well as your recommendation messages. Also presented is an overview of the current weather based on user location.

LoginActivity - The page presented upon launch of the app, given a user is not logged in. Presents a prompt for e-mail and password, as well as a Create Account button.

MainActivity - The container in which all fragments of the application are placed. It is a base of the main BeneFITS application.

MapFragment - Fragment of MainActivity used for the What's Nearby page. Shows the user's location and can query gyms, parks, and trails nearby the user's radius for workout purposes.

Registration - Page presented upon hitting the Create Account button on LoginActivity. Prompts the user for registration information and pushes that information as a new Firebase entry, creating a new valid user login.

SettingsFragment - Fragment of MainActivity used to adjust user settings.

SplashActivity - Presented upon loading of BeneFITS application, prior to presentation of the LoginActivity page.

User - Class used for assistance in creating a Firebase entry, on call to Create Account on LoginActivity.