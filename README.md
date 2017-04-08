# ATS_Nearby_Reboot

`Because, I can't think of a better name and since it's a partial fork of the previous project`

## Background Information
 
The project was written to combat attendance fraud in Singapore Polytechnic. Every the start of every lesson, the lecturer will generate a One Time Pin(OTP) and display it on the projector in plain text. Students will then submit the OTP to a HRMS portal and they will be marked present for the lesson. Obviously this is flawed since students are able to help submit the attendance code for their friends by sharing their account credentials. Lecturers will waste time occasionally by checking for fraud. It defeats the purpose of marking the attendance electronically. Instead of doing nothing but complain and whine or making a useless [petition](https://www.change.org/p/mr-tan-choon-shian-ms-georgina-phua-stopspatssystem), why not do something about it?
 
## About This Project

This project was a forked of our previous project[(ATS_Nearby)](https://github.com/emansih/ATS_Nearby). The current project uses WiFi and ultrasound to take student's attendance. You may notice that bluetooth is no longer required since pairing increases friction in User Experience(UX). The previous project was using Google's proprietary Nearby API. The API itself isn't bad. However, there were limitations in what the API could do. Nearby API had a restriction on the number of API calls you can call in a period of time and it relies on Google Play services. With a reliance on Google Play Services, it means that it will never be able to truly run on all platforms. It defeated the purpose of our original goal which is 

> to build the software with compatability in mind

In theory, this software should be able to run on Amazon Fire devices or any non-certified [GMS devices](https://www.android.com/gms/). The web version written in Javascript is coming soon. This sofware has also moved away from Firebase's BaaS to a self hosted BaaS instance. This allows the user / organization to better control the data flowing out of the network. 

## How to get this project running on my device?!

Easy: 
```bash
$ repo init -u git@github.com:Proximity-Attendance-System/Repo-Manifest.git && repo sync
```

Advance:
1. Download this repo
2. Download Quiet(msequence_ordering) `git clone https://github.com/quiet/org.quietmodem.Quiet.git -b msequence_ordering`

Place Quiet in the following hierarchy order. `../../quiet/android/quiet` **OR** edit [settings.gradle](settings.gradle)

###### IMPORTANT NOTE: The app itself will point to http://setsuna.asdf.com by default. Either change the url or edit your hosts file and point to setsuna.asdf.com

Personally, I am using [Pi-hole](https://pi-hole.net/) as my DNS server. I simply edit the host file on my Raspberry Pi and add in  `192.168.1.29 setsuna.asdf.com`. 

`192.168.1.29` is my VM running the LAMP stack. 

That's it. 

Special thanks to Mr. Teo Shin Jen my lecturer for guidance in this project. Let's not forget about Mr. Brian Armstrong for quiet library, which was essential for the project.
