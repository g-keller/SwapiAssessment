# SwapiAssessment

SwapiAssessment is an Android application fulfilling three basic requirements:

1) Access SWAPI (the Star Wars API) to get a list of Star Wars characters.
2) Display the characters in a scrollable list. Every list item should show a character's name, height, mass, and birth year.
3) Show a toast containing a given character's eye color whenever the user taps on that character's list item.

In addition to the bare-bones features, SwapiAssessment provides a Star Wars-themed UI and special Star Wars-themed list scroll functionality (be sure to press the play button in the app's action bar to check it out!).

## Notes

Though helper libraries existed to more easily consume SWAPI, I chose to use the API endpoints directly for demonstration purposes. That being said, if this were a serious application, I may still have reservations about using these helper libraries. The Android helper library hasn't been updated in several years, and in general it's a risk to expect the authors of these helpers to keep up with changes to the actual API.

Google recommends Kotlin for starting new applications (see https://developer.android.com/kotlin/first), but I chose to utilize Java instead because the majority of the codebase I'd be working with if I got this job is written in Java. 

Here are the credits for the special fonts I used in this application!

[Star Jedi Font](https://www.dafont.com/star-jedi.font)

[News Cycle Font](https://fonts.google.com/specimen/News+Cycle)

