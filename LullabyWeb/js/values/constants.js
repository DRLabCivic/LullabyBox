define([], function(){
	var constants = {

			/*
			 *  server settings 
			 */
			"web_service_url": "api.php",
			"settings_web_timeout" : 2000,
			"max_file_size" : 25 * 1024 * 1024, //in bytes
			"web_data_folder" : "recordings/"
			//"web_data_folder" : "http://sahabe.mooo.com/lullaby/recordings/"
	};
	return constants;
});