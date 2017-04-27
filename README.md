## Background Information
 
Check out [this link](https://asdfdev.github.io/background.html) for more info
 
## About This Project

This project was a forked of our previous project[(ATS_Nearby)](https://github.com/emansih/ATS_Nearby). The current project uses WiFi and ultrasound to take student's attendance. You may notice that bluetooth is no longer required since pairing increases friction in User Experience(UX). The previous project was using Google's proprietary Nearby API. The API itself isn't bad. However, there were limitations in what the API could do. Nearby API had a restriction on the number of API calls you can call in a period of time and it relies on Google Play services. With a reliance on Google Play Services, it means that it will never be able to truly run on all platforms. It defeated the purpose of our original goal which is 

> to build the software with compatability in mind

In theory, this software should be able to run on Amazon Fire devices or any non-certified [GMS devices](https://www.android.com/gms/). This sofware has also moved away from Firebase's BaaS to a self hosted BaaS instance. This allows the user / organization to better control the data flowing out of the network. 

## How to get this project running on my device?!

Easy: 
```bash
$ repo init -u git@github.com:Proximity-Attendance-System/Repo-Manifest.git && repo sync
```

Advance:
1. Download this repo
2. Download Quiet(msequence_ordering) `git clone https://github.com/asdf/org.quietmodem.Quiet.git -b msequence_ordering`

Place Quiet in the following hierarchy order. `../../quiet/android/quiet` **OR** edit [settings.gradle](settings.gradle)

###### IMPORTANT NOTE: The app itself will point to http://setsuna.asdf.com by default. Either change the url or edit your hosts file and point to setsuna.asdf.com

Personally, I am using [Pi-hole](https://pi-hole.net/) as my DNS server. I simply edit the host file on my Raspberry Pi and add in  `192.168.1.29 setsuna.asdf.com`. 

`192.168.1.29` is my VM running the LAMP stack. 

That's it. 

Special thanks to Mr. Teo Shin Jen my lecturer for guidance in this project. Let's not forget about Mr. Brian Armstrong for quiet library, which was essential for the project.


