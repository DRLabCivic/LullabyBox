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
			'click .recording-item' : 'onCloseButtonPressed'
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
			
			//register audioplayer events
			var audioplayer = this.$('#audioplayer');
			//audioplayer[0].play();
			audioplayer.on('canplaythrough', {self: this}, this.onLoadedAudioFile);
			audioplayer.on('loadstart', {self: this}, this.onLoadingAudioFile);
			this.$('#wavsrc').on('error', {self: this}, this.onAudioFileError);
		},
		
		onCloseButtonPressed: function() {
			this.destroy();
		},
		
		onLoadedAudioFile: function(event) {
			var self = event.data.self;
			
			var audioplayer = event.target;
			audioplayer.play();
			
			self.spinner.stop();
			self.$('.centered-text').show();
		},
		
		onLoadingAudioFile: function(event) {
			var self = event.data.self;
			
			self.$('.centered-text').hide();
			
			var target = self.$('#spinner')[0]
			self.spinner = new Spinner().spin(target);
		},
		
		onAudioFileError: function(event) {
			var self = event.data.self;
			
			self.$('.centered-text').html("<h2>:(</h2>");
			self.$('.centered-text').show();
			
			self.spinner.stop();
		}

	});
	// Our module now returns our view
	return RecordingDialogView;
	
});