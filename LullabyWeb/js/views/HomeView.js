define([
	'jquery',
	'underscore',
	'marionette',
	'models/RecordingCollection',
	'views/MapView',
	'text!templates/homeTemplate.html'
], function($, _, Marionette, RecordingCollection, MapView, template){
	
	var HomeView = Backbone.Marionette.LayoutView.extend({
		
		initialize: function(options) {
			
			this.recordings = new RecordingCollection();
			this.recordings.fetch();
			//this.recordings.stream({interval: 10000});
		},
		
		regions: {
			mapRegion : '#map-container',
			dialogRegion: '#dialog-container'
		},
		
		template: _.template(template),
		
		onShow: function() {
			this.showMap();
		},
		
		showMap: function() {
			view = new MapView({collection: this.recordings})
			this.getRegion('mapRegion').show(view);
		},
		
		
		
		
	});
	// Our module now returns our view
	return HomeView;
	
});