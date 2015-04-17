<?php

    require_once "config.php";
    require_once "utils.php";

    if ($_SERVER['REQUEST_METHOD'] == "POST")
        submitFile();
    else if ($_SERVER['REQUEST_METHOD'] == "GET")
        getRecordings();
    else
        _sendData("No valid request",400);

    function getRecordings() {

        $db = _getDbConnection();

        //get data
        $stmt = $db->query("SELECT * FROM ".DB_TABLE_RECORDINGS." ORDER BY id");
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        //close db
        $db = null;

        _sendData($result,200);
    }

    function submitFile() {

        if (isset($_FILES['file']) && isset($_POST['location'])) {

            $file = $_FILES['file'];
            $location = $_POST['location'];

            //decode json
            try {
                $location = json_decode($location,true);
            } catch (Exception $e) {
                _sendData("Error parsing json",500);
            }

            //try to upload file
            _uploadFile($file);
                
            //insert into database
            _insertToDatabase($location,$file['name']);

            _saveToLog($file['name']." uploaded successfully.");
            _sendData(array(),200);

        } else {
            _saveToLog("No file and location submited");
            _sendData("No file and location submited",400);
        }

    }

    