LullabyBox
==========

## Installation

After pulling the repository: modify *config_default.php* to point to your sql server and rename it to *config.php*!

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

**Important:** Take care that the sql file is saved with utf-8 encoding.

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

## Create map tiles

1. First create map with TileMill and export to .mbtiles. Download: <https://www.mapbox.com/tilemill/>
2. Install mbutil from <https://github.com/mapbox/mbutil>
    * `sudo easy_install mbutil`

Example: export xx.mbtiles into mapTiles/ with 

``` 
mb-util xxx.mbtiles map_tiles --image_format=png
```

## TODO

### App

* [x] fix special asci characters in location string
* [x] fix that a soundfile for each recording is created, instead only use 
temp file
    * now the audiofile gets deleted after succesfull upload.
* [ ] restrict maximal length of soundfile
* [ ] Make it look nicer

### Web