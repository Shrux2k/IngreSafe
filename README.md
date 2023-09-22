Before you begin, ensure that you have the following prerequisites in place:

1. Android Studio: Make sure you have Android Studio installed on your computer. You can download it from the official Android Studio website (https://developer.android.com/studio).

2. Git: Ensure that Git is installed on your system. You can download Git from the official Git website (https://git-scm.com/).

•	Step 1: Clone the Repository

1. Open Android Studio and ensure it is up to date with the latest updates and plugins.

2. Click on "File" in the top menu, then select "New" and choose "Project from Version Control."

3. From the dropdown menu, select "Git."

4. In the "Repository URL" field, enter the repository link provided for Ingresafe. 
It should look something like this:
   
   https://git.cs.bham.ac.uk/projects-2022-23/sxs2012
   `
5. Choose a directory where you want to clone the repository on your local machine by clicking the "Folder" icon.

6. Click "Clone" to initiate the cloning process. Android Studio will download the repository files to your chosen directory.

•	Step 2: Configure the Project

1. Once the cloning process is complete, Android Studio will prompt you to open the project. Click "Open" to proceed.

2. Android Studio may detect that the project uses a specific version of the Android Gradle plugin. Follow the prompts to upgrade the project to the recommended Gradle version, if necessary.

3. After the project is loaded, click on the "Sync Project with Gradle Files" icon in the top toolbar to ensure that all dependencies are resolved.

•	Step 3: Set Up an Emulator or Connect a Physical Device

To run Ingresafe, you need to set up an Android emulator or connect a physical Android device to your computer. Here's how to do it:

Emulator:

1. In Android Studio, go to "Tools" > "AVD Manager."

2. Click on "Create Virtual Device" to create a new emulator.

3. Follow the setup wizard to choose a device type, system image, and other configuration options.

4. Click "Finish" to create the emulator.

Physical Device:

1. Enable Developer Options on your Android device. This typically involves tapping the "Build Number" in the device's "About Phone" settings multiple times.

2. Connect your Android device to your computer using a USB cable.

3. In Android Studio, select your connected device from the list of available devices for deployment.

•	Step 4: Build and Run Ingresafe

1. In Android Studio, ensure that the correct emulator or physical device is selected for deployment.

2. Click the green "Run" button (or press Shift + F10) to build and run the Ingresafe app on the selected device.

3. Wait for Android Studio to build the app and deploy it to the emulator or device. You should see the Ingresafe app launch on the screen.

4. You can interact with the app on the emulator or device just as you would with any other Android application.
