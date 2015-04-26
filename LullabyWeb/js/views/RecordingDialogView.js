define([
	'jquery',
	'underscore',
	'marionette',
	'text!templates/recordingTemplate.html'
], function($, _, Marionette, template){
	
	var RecordingDialogView = Backbone.Marionette.ItemView.extend({
		
		id: 'recording',
			
		template: _.template(template),
		
		className: 'modal-dialog hidden transition',
		
		events: {
			'click #closeButton' : 'onCloseButtonPressed'
		},
		
		onRender: function() {
			
			var self = this;
			setTimeout(function() {
				self.$el.removeClass('hidden');
			},50);
			this.$el.on("webkitTransitionEnd transitionend", function(event) {
				self.$el.removeClass('modal-dialog');
				self.$el.off('webkitTransitionEnd transitionend');
			});
		},
		
		onCloseButtonPressed: function() {
			this.destroy();
		}

	});
	// Our module now returns our view
	return RecordingDialogView;
	
});