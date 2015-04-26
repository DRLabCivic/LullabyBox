define([
	'jquery',
	'underscore',
	'marionette',
	'models/RecordingCollection',
	'views/MapView',
	'views/RecordingDialogView',
	'text!templates/homeTemplate.html'
], function($, _, Marionette, RecordingCollection, MapView, RecordingDialogView, template){
	
	var HomeView = Backbone.Marionette.LayoutView.extend({
		
		initialize: function(options) {
			
			this.recordings = new RecordingCollection();
			this.recordings.stream({interval: 10000});
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
			view.on('show:recordingPopup',this.showRecordingPopup,this);
			this.getRegion('mapRegion').show(view);
		},
		
		showRecordingPopup: function(model) {
			var view = new RecordingDialogView({model: model})
			//this.listenTo(view,'destroy',this.unfocusMarker);
			this.getRegion('dialogRegion').show(view);
		}
		
	});
	// Our module now returns our view
	return HomeView;
	
});