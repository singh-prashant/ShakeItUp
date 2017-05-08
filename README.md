# ShakeItUp
Show word of the day from Wordnik. User can see the definition of the word by shaking the device. User and word informations are persisted in the local storage as well as amazon aws server. 

When user open the app, it pulls today's word from Wordnik. This information persisted in the sqlite database. Also there is a schedule mechanism available which pulls data repeatedly after 3-4 hours. So that each time user open the app they don't need to pull data. 


App architecture?
App pulls data from server, then parse the data and insert it into the local storage. Whenever data is inserted, ui is notified through contentprovider. 
