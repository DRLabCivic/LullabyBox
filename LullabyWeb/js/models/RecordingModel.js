define([
        'underscore',
        'backbone',
        'values/constants'
], function(_, Backbone, Constants){

	var RecordingModel = Backbone.Model.extend({
		
		defaults: {
			id: -1,
			longitude: 0,
			latitude: 0
		},
		
		toGeoJSON: function() {
			var self = this;
			var json = {
				"type": "Feature",
				"geometry": {
					"type": "Point",
					"coordinates": [self.get('longitude'), self.get('latitude') ]
				},
				"properties": {
					"id": self.get('id'),
					"className": "marker color-0",
					"type": "marker"
				}					
			};
			return json;
		},
		
	});

	// Return the model for the module
	return RecordingModel;

});