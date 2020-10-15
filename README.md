# indproj

Individual project by Sandra Kullar

# Set up the project

To run the app, you need to create a firebase account and create a new project and connect the code to the project. 

You can follow step 1 and 2 from this guide https://firebase.google.com/docs/android/setup.

Also make sure to enable email-authentication 


You also need to change the rules in the database:

```
{
  "rules": {
    ".read": "now < 1603231200000",
    ".write": "now < 1603231200000",
  }
}
```

And in the storage

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```

To run the app you need to connect either an android-phone to the computer or install an android emulator. 

You can check out this link for more information about how to install an android emulator: https://developer.android.com/studio/run/emulator


# Demo


A demo of the app is found here:

https://drive.google.com/file/d/15hNHbgn1ma8cX_bEWrioAqsgiy4-CsmX/view?usp=sharing
