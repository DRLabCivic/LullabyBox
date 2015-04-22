LullabyBox
==========

## Location Database

Reads database file from *cities.sql* in the externalStorageDirectory (usualy /sdcard/ or /mnt/sdcard)

### cities.sql content:

```
combined	population	country_code	region	latitude	longitude
Aixàs, Andorra	0	ad	06	42.4833333	1.4666667
Aixirivali, Andorra	0	ad	06	42.4666667	1.5
...
```

All values are tab seperated.

## Webserver Settings

### php config

In the *php.ini* the following values have to be changed:

```
; Maximum allowed size for uploaded files.
upload_max_filesize = 40M

; Must be greater than or equal to upload_max_filesize
post_max_size = 40M
```  

also uncomment the line `;extension=pdo_mysql.so`.  
on arch linux its placed in */etc/php/php.ini*


### nginx config

add `client_max_body_size 40M;` to  */etc/nginx/nginx.conf* inside the server {} block.

## TODO

### App

* [ ] fix special asci characters in location string
* [x] fix that a soundfile for each recording is created, instead only use temp file
* [ ] restrict maximal length of soundfile
* [ ] Make it look nicer

### Web