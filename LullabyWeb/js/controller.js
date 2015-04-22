define([
        'jquery',
        'marionette',
        'vent',
        'views/HomeView'
], function($, Marionette, Vent, HomeView){
	
	var Controller = Marionette.Controller.extend({
		
		initialize: function(app) {
			this.app = app;
			
			app.addRegions({
				containerRegion: {
					selector: "#container",
				},
				modalRegion: {
					selecor: "#modal"
				}
			});
			
			this.homeView = new HomeView();
		},
		
			
		/* ROUTES */
		
		recording: function(id) {
			this.homeView.showRecording(id);
		},
	
		defaultRoute: function() {
			this.app.containerRegion.show(this.homeView)
		},
		
	});
	
	return Controller;
});