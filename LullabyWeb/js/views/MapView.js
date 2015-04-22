define([
	'jquery',
	'underscore',
	'marionette',
	'text!templates/mapTemplate.html'
], function($, _, Marionette, template){
	
	var MapView = Backbone.Marionette.ItemView.extend({
		
		initialize: function(options) {
			
			//register collection events
			this.model = options.model;this.collection = options.collection;
			this.collection.on('add', this.addFeature, this);
			this.collection.on('reset', this.onCollectionSync,this);
			
			//create marker object so collection can add to
			this.markers = new L.MarkerClusterGroup({
				maxClusterRadius : 25,
				showCoverageOnHover: false,
				spiderfyOnMaxZoom: true
			});
			
		},
		
		template: _.template(template),
		
		events: {
		},
		
		onShow: function() {
			this.map = L.map(this.$('#map')[0], {
				center: [ 52,16],
				maxZoom: 5,
				minZoom: 5,
				zoom: 5,
				zoomControl: false
			});
			
			// add custom map layer
			L.tileLayer('map_tiles/{z}/{x}/{y}.png', {
				attribution: '-',
				minZoom: 1,
				maxZoom: 8,
				//opacity: 0.5,
			}).addTo(this.map);
			
			this.markers.addTo(this.map);
			
		},
		

		onCollectionSync: function() {
			this.collection.each(this.addFeature,this);
		},
		
		addFeature: function(model) {
			var geoJson = model.toGeoJSON();
			if (geoJson) {
				var layer = L.geoJson(geoJson,this.geoLayerOptions);
				this.markers.addLayer(layer);
			}
		},
		
	});
	// Our module now returns our view
	return MapView;
	
});