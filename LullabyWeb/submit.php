<?php

    define('DIR_RECORDINGS', "recordings");

    if (isset($_FILES['file'])) {

        $file = $_FILES['file'];

        //move file to records directory
        $upload_dir = DIR_RECORDINGS;

        if (is_uploaded_file($file['tmp_name'])) {
            move_uploaded_file($file['tmp_name'], $upload_dir.'/'.$file['name']);
            _saveToLog($file['name']." uploaded successfully.");
            _sendData(array(),200);
        }
        _sendData(array(),404);
    } else {
        _saveToLog("No file submited");
        _sendData(array(),404);
    }
    
    // Log data
    function _saveToLog($data) {
        $file = 'log/log.txt';
        $current = file_get_contents($file);
        $current .= "Time: ".date("r")."\n Data:".$data."\n";
        file_put_contents($file, $current);
    }

    function _sendData($data = array(), $status = 200, $onlyFirst = false) {
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
        if ($onlyFirst && !empty($data))
            echo json_encode($data[0]);
        else
            echo json_encode($data);

        //end script
        exit(); 
    }

    function _requestStatus($code) {
        $status = array(  
            200 => 'OK',
            404 => 'Not Found',   
            405 => 'Method Not Allowed',
            500 => 'Internal Server Error',
        ); 
        return ($status[$code])?$status[$code]:$status[500]; 
    }
?>