define([
	'jquery',
	'underscore',
	'marionette',
	'text!templates/recordingTemplate.html'
], function($, _, Marionette, template){
	
	var RecordingDialogView = Backbone.Marionette.ItemView.extend({
		
		_spinner: new Spinner(),
		_audioPlayer: null,
		
		id: 'recording',
			
		template: _.template(template),
		
		className: 'modal-dialog hidden transition',
		
		events: {
			'click .recording-item' : 'onCloseButtonPressed',
			'click .center-item' : 'onCenterItemPressed'
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
			
			//setup audioplayer
			this._audioPlayer = this.$('#audioplayer')[0];
			
			// register player events
			this.$('#audioplayer').on('canplaythrough', {self: this}, this.onLoadedAudioFile);
			this.$('#audioplayer').on('loadstart', {self: this}, this.onLoadingAudioFile);
			this.$('#wavsrc').on('error', {self: this}, this.onAudioFileError);
		},
		
		onBeforeDestroy: function() {
			if (this._audioPlayer != null)
				this._audioPlayer.pause();
				this._audioPlayer.setAttribute('src',null);
		},
		
		onCloseButtonPressed: function() {
			this.destroy();
		},
		
		onLoadedAudioFile: function(event) {
			var self = event.data.self;
			
			self._audioPlayer.play();
			
			self._spinner.stop();
			self.$('.centered-text').show();
		},
		
		onLoadingAudioFile: function(event) {
			var self = event.data.self;
			
			self.$('.centered-text').hide();
			
			var target = self.$('#spinner')[0]
			self._spinner.spin(target);
		},
		
		onAudioFileError: function(event) {
			var self = event.data.self;
			
			self.$('.centered-text').html("<h2>:(</h2>");
			self.$('.centered-text').show();
			
			self._spinner.stop();
		},
		
		onCenterItemPressed: function() {
			console.log('pressed');
			if (this._audioPlayer.paused)
				this._audioPlayer.play();
			else
				this._audioPlayer.pause();
			return false;
		}

	});
	// Our module now returns our view
	return RecordingDialogView;
	
});