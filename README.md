# Weight Tracker App
## Requirements and Goals
This app is designed for people who wish to get a closer examination of their weight loss journey. Once a day, the user will enter their weight and the results will be displayed on a line graph. Over time, the user can visually see a weight trend to determine of their diet or workout routine is efficient. The user will enter their current weight along with their goal weight in the profile settings. As more entries are made, a total weight loss and a weekly weight loss counter is displayed above the line chart. 
## Screens and Features
UI consists of 3 views, Home, Profile, and Settings with a Floating Action Button to add weights. Upon installed, user will create a username and password. If new user, select Register and Sign In. Selecting Remember Me will keep the current user signed in until user clicks Logout in the Settings page.

![1 Login](https://github.com/user-attachments/assets/edc69bee-c38a-42c7-9cdb-6f033f374fe2)

Once successfully logged in, users starting weight and goal weight must be obtained to configure y-axis on graph. A dialog message will alert user to set up profile, clicking Lets Go will automatically open Profile page.

![2 First Time Login](https://github.com/user-attachments/assets/e0adacf9-f2cb-4ec6-97fd-a5b08585b55b)

The 'Default' profile is set to Michael Scarn, but once the user enters their information correctly and clicks Update, the page will refresh with the users information stored. Below is the default profile.

![3 Default Profile](https://github.com/user-attachments/assets/4bc4a75a-1e2c-44bf-a9a9-81131cef3369), ![4 Default Profile scrolled](https://github.com/user-attachments/assets/4c52d938-0bce-48a7-83cf-a3fa0e9c2b0d)

Once the profile has been updated, in this case the starting weight is 240 and the goal is 200, the new line graph is displayed on the home screen.

![6 New Home](https://github.com/user-attachments/assets/f61ab10b-6f9c-4cb8-adae-7618846f0009)

Clicking the floating action button will display a new dialog message where the user can select the current date and enter their changed weight which will add to the line graph.

![7 Add weight](https://github.com/user-attachments/assets/93584fea-92dd-466c-afc1-f2fbe365f738)

The Settings page can logout the user, change the password, manually change the theme (by default it is set by the devices current theme mode), and enable or disable notifications. 

![8 Settings](https://github.com/user-attachments/assets/bc392bb3-d723-422f-ab92-e16746ff7cfa)

## Challenges
Incorporating the line graph, using a bottom naviation bar, and fragments were all new experiences that came with their own unique challenges. Vigorous testing and researching ensured that the app should function in the ascpets it was designed for. Their are still functions that are not working properly, which will be addressed in the near future. For example, the line graph initially shows the x-axis as 30, as in 30 days in a month. Once a new weight has entered, the x-axis is no longer visable, rendering any entries either useless or unavailable. Future updates will address this bug.






