# Pokedex
App to Consume [PokeApi](https://pokeapi.co/) and make searches by name. made with Jetpack Compose.

# Application ScreenShots

![flow1](https://user-images.githubusercontent.com/42783065/193973080-87e966ba-0505-44e2-9ed3-fd207eb7a987.jpeg)

![flow2](https://user-images.githubusercontent.com/42783065/193973096-14e11847-9f71-4135-a693-1d00ed81d93b.jpeg)

![flow3](https://user-images.githubusercontent.com/42783065/193973106-c4a1d5c7-0710-4edd-aed5-9d0cd9a8e703.jpeg)

![flow4](https://user-images.githubusercontent.com/42783065/193973110-2aa2988a-18af-44c0-b03b-b4a59c82b6b3.jpeg)

![flow5](https://user-images.githubusercontent.com/42783065/193973128-3786c143-0ff0-4b6c-a6ed-82c59b08a524.jpeg)

![flow6](https://user-images.githubusercontent.com/42783065/193973140-8023ed4b-c886-4bb1-af65-87c7c0e81ae6.jpeg)

# Challenge Objectives

- Build app to search and see details of the pokemons using [PokeApi](https://pokeapi.co/) and [PokeApiSprites](https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png). ✓
- Get 150 pokemons. ✓
- Search pokemons by name. ✓
- Be able to search pokemons without internet connection. ✓
- Store the pokemons list carried out locally and offer to repeat them in case of not having an internet connection. ✓
- Generate submenus on details screen. ✓
- Show type, evolution details, moves, stats, encounters of the selected pokemon. ✓

# Issues Found During the challenge

- Map some pokeapi models to UI designs and the amount of information.
- Couldn't create a screen only with moves, since in this context I mustn't pass "list of moves" as argument.
- Dynamic colors for screens.
- Having a nice cache control without androidx remote mediator.
- Having a nice real-time Network management.

# Solutions to Issues

- Reading deeply Pokeapi documentation to understand the collected information and show the necessary info in the app (easily to pokemon's fans).
- Using BottomSheetScaffold in the same screen to avoid passing the entire list as argument.
- Using Palette library, this one provide methods to get dominant color on any image.
- Using cache control class (cacheControlResult.kt) based on [codingFlow class](https://github.com/codinginflow/MVVMNewsApp/blob/Part-15_Why-and-How-to-Handle-Process-Death/app/src/main/java/com/codinginflow/mvvmnewsapp/util/NetworkBoundResource.kt).
- Making a network monitor class based on [Shreyas Patil](https://medium.com/scalereal/observing-live-connectivity-status-in-jetpack-compose-way-f849ce8431c7) (Google Developer Expert) article.

# Non-native libraries used

- [Truth](https://truth.dev/) for better assertions in testing.
- [Retrofit 2](https://square.github.io/retrofit/) to handle API calls easily.
- [Coil](https://coil-kt.github.io/coil/compose/) provides an AsyncImage component to handle images from URLs easily.
- Palette to get the main color in every pokemon image.

# Test Cases

- Testing Search Bar - tested search bar, working as expected.
- Testing Room database - tested all methods to read/write from database.
- Testing List Screen - tested if screen is displayed when list is not empty.
- Testing Utils method - tested methods to format strings and get specific string values.
- Testing Utils methods to get colors - tested all methods to get colors by query.
- Testing connectivity status - tested if connectivity status function is working.

# Used Devices for testing

- One Plus 7T - Android 11 (Physical)
- Xiaomi Poco X3 Pro - Android 12 (Physical)
- Samsung Galaxy S21 - Android 12 (Physical)
- Blu 3 - Android 8 (Physical)
- Pixel 4 - Android 11 (Emulator)
- Pixel XL - Android 10 (Emulator)
- Nexus 5 - Android 9 (Emulator)
- Pixel 4a - Android 11 (Emulator)

# Video App Demo

- [Application Video Demonstration](https://drive.google.com/file/d/1JTghyTiT4tzfUxaqMbbax-kv5S7nGSsV/view?usp=sharing)


## License

Open Source
