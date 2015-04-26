<?php

    require_once "config.php";

    function _uploadFile($file) {
        //check if filetype is ok
        _saveToLog($file['type']);
        if (!($file['type'] == "audio/wav" || $file['type'] == "application/octet-stream"))
            _sendData("File not allowed.",415);


        if (!is_uploaded_file($file['tmp_name']))
            _sendData("Error copying file to server",500);
        
        //move uploaded file
        move_uploaded_file($file['tmp_name'], DIR_RECORDINGS.'/'.$file['name']);
    }

    function _insertToDatabase($location,$fileName) {
        // get db connection
        $db = _getDbConnection();

        //insert
        $stmt = $db->prepare("INSERT INTO ".DB_TABLE_RECORDINGS." 
            (file,city,country,longitude,latitude,population)
            VALUES (:file,:city,:country,:longitude,:latitude,:population)");

        $insertData = array(
            'file' => $fileName,
            'city' => $location['city'],
            'country' => $location['country'],
            'longitude' => $location['longitude'],
            'latitude' => $location['latitude'],
            'population' => $location['population']
        );
        $stmt->execute($insertData);

        //close db
        $db = null;
    }

    function _getDbConnection() {
        $dbhost=DB_SERVER;
        $dbuser=DB_USER;
        $dbpass=DB_PASSWORD;
        $dbname=DB_DATABASE;
        
        try {
	        $dbh = new PDO("mysql:host=$dbhost;dbname=$dbname", $dbuser, $dbpass);  
	        $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	        return $dbh;
        } catch (Exception $e) {
        	echo "Could not connect to database.";
        }
    }
    
    // Log data
    function _saveToLog($data) {
        $file = 'log/log.txt';
        $current = file_get_contents($file);
        $current .= "Time: ".date("r")."\n Data:".$data."\n";
        file_put_contents($file, $current);
    }

    function _sendData($data = array(), $status = 200) {
        // headers for not caching the results
        header('Cache-Control: no-cache, must-revalidate');
        header('Expires: Mon, 26 Jul 1997 05:00:00 GMT');

        // allow all requests
        header("Access-Control-Allow-Orgin: http://farseer.de");
        header("Access-Control-Allow-Methods: *");
        
        // headers to tell that result is JSON
        header('Content-type: application/json');

        //send status
        header("HTTP/1.1 " . $status . " " . _requestStatus($status));

        // send the result now
        echo json_encode($data);

        //end script
        exit(); 
    }

    function _requestStatus($code) {
        $status = array(  
            200 => 'OK',
            400 => 'Bad request',
            404 => 'Not Found',
            409 => 'Conflict',
            415 => 'Unsupported Media Type',   
            405 => 'Method Not Allowed',
            500 => 'Internal Server Error',
        ); 
        return ($status[$code])?$status[$code]:$status[500]; 
    }