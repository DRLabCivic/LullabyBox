define([
        'underscore',
        'backbone',
        'models/RecordingModel',
        'values/constants'
], function(_, Backbone, RecordModel, Constants){
	
	RecordingCollection = Backbone.Collection.extend({
		model: RecordModel,
		
		url : Constants['web_service_url'],
		
		parse: function(response, options) {
			return response.recordings;
		},
		
		stream: function(options) {
			// Cancel any potential previous stream
			this.unstream();
			
			var _update = _.bind(function() {
				
				var self = this;
				options.success = function() {
					self.isLoading = false;
				};
				
				this.fetch(options);
				this._intervalFetch = window.setTimeout(_update, options.interval || 1000);
			}, this);

			_update();
		},

		unstream: function() {
			window.clearTimeout(this._intervalFetch);
			delete this._intervalFetch;
		},

		isStreaming: function() {
			return _.isUndefined(this._intervalFetch);   
		},
	
	});
	
	return RecordingCollection;
});