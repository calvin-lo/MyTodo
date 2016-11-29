# CSCI4100U/SOFE4870U Final Project - MyTodo

### Contributors
Calvin Lo (100514352) <br />
Karan Chandwaney (100472699)

### App Details
The Android application within this repository is the Final Project submission for Mobile Devices. It is a location based task management app that lets users add tasks to a checklist. When the task is added, the user's location is determined and associated with the task, and can be viewed on a map. Other features include displaying the weather at the task location.

### Instructions for Use
When the app is opened for the first time, the user is prompted with a permission request to access location details. Upon accepting, the app's main screen is displayed. Tasks added in the app will be displayed here.

**Tasks can be added** by tapping the yellow button at the bottom, of the screen and can be marked as completed by tapping the checkbox beside the task. Tapping the menu button on the top left of the screen or swiping right (from the left edge of the screen) brings up a filter menu to choose what tasks are to be displayed on the main screen (complete, incomplete, etc.).

**Options about the task** can be viewed by tapping and holding on the task name in the list view. An option to view task details is displayed, as well as options to delete or hide the task. Tapping the Settings button, accessible from the top right gives the user the option to clear the database of tasks.

**Tasks can be viewed on a map** by Swiping to the "MAP" tab, which brings up a map view and displays where various tasks were added. When tasks are added, a red marker is displayed on the map at the location the task was added. Tapping on the red marker shows what task was added.

**The details of the task** are displayed when the user taps on Details (after tapping and holding on the task). This includes the location the task was added, the created time and due time of the task, whether or not the task is complete, and the weather details of the location of the task. Tapping the yellow button at the bottom also gives the user options to edit the task (mark as complete, incomplete, etc.).

### API Reference
Two APIs were used in the development of this app:

Google Maps API - https://developers.google.com/maps/ <br />
OpenWeatherMap API - http://openweathermap.org/api

### Project Requirements Met
**Basic Requirements**
* The app consists of multiple activities.
* The app incorporates media playback through the use of a click sound when a task is saved.
* The app makes use of internet resources, by using the aforementioned APIs to display and interact with a map view and to get weather data.
* Storage - The tasks are stored and displayed through the use of a database.

**Other Features Implemented**
* 2D graphical content through the use of ImageButtons and weather icons.
* GPS and geocoding are used to get the location of the user.
