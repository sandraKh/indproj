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

# Demo


A demo of the app is found here:

https://drive.google.com/file/d/13cneQFrXeggFHl2Kkr4w2_4kU9zEqvK0/view?usp=sharing 
