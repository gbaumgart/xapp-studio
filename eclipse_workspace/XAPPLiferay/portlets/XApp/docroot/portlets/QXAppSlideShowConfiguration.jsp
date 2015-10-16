<%@ include file="./init.jsp" %>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<aui:form action="<%= "as" %>" method="post">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<aui:layout>
		<aui:column columnWidth="50" id="controls">
			<div class="aui-field-row">
				<aui:input cssClass="url" inlineField="true" label="url" name="preferences--url--" value="<%= url %>" />
			</div>

		</aui:column>

		<aui:column columnWidth="50">
			<div class="aui-field-wrapper-content" id="preview"></div>
		</aui:column>
	</aui:layout>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-color-picker,aui-datatype,aui-swf">
	var createPlayer = function() {
		var id = urlToVideoId(urlNode.val());
		var height = parseInt(heightNode.val(), 10) || 0;
		var maxWidth = (formNode.get('clientWidth') || formNode.get('scrollWidth')) - (controlsNode.get('clientWidth') || controlsNode.get('scrollWidth'));
		var playerOptions = {
			border: showThickerBorderNode.val(),
			cc_load_policy: closedCaptioningNode.val(),
			color1: encodeHex(borderColorNode.val()),
			color2: encodeHex(playerColorNode.val()),
			disablekb: (!A.DataType.Boolean.parse(enableKeyboardControlsNode.val())).toString(),
			egm: enableEnhancedGenieMenuNode.val(),
			fs: enableFullscreenNode.val(),
			hd: hdNode.val(),
			iv_load_policy: annotationsNode.val(),
			rel: enableEnhancedGenieMenuNode.val(),
			showinfo: showInfoNode.val(),
			showsearch: enableSearchNode.val(),
			start: startTimeNode.val()
		};
		var width = parseInt(widthNode.val(), 10) || 0;

		var playerOptionsCompiled = [swfURL + id];
		var ratio = Math.min(maxWidth / width, 1);

		height = Math.floor(height * ratio);
		width = Math.floor(width * ratio);

		for (var i in playerOptions) {
			if (playerOptions[i]) {
				playerOptionsCompiled.push(i + '=' + playerOptions[i].replace(/^true$/, '1').replace(/^false$/, '0'));
			}
		}

		if (id) {
			previewNode.setContent(['<a href="', watchURL, id, '" rel="external" title="watch-this-video-at-youtube"><img src="', imageURL.replace('<%= id %>', id), '" alt="youtube-video" width="100%" height="100%" /></a>'].join(''));

			new A.SWF(
				{
					boundingBox: previewNode,
					height: height,
					url: playerOptionsCompiled.join('&'),
					width: width,
					version: 0
				}
			).render();
		}
		else {
			previewNode.setStyles(
				{
					height: height,
					width: width
				}
			);
		}
	};

	var encodeHex = function(hex) {
		return (hex) ? '0x' + hex.replace('#', '').replace(/^(.)(.)(.)$/, '$1$1$2$2$3$3').toLowerCase() : '';
	};

	var presetChange = function(e) {
		if (this.val().indexOf('x') < 0) {
			A.one('.aui-field.height').removeClass('invisible');
			A.one('.aui-field.width').removeClass('invisible');

			return;
		}

		var dimensions = this.val().split('x');

		heightNode.val(dimensions[1]);
		widthNode.val(dimensions[0]);

		createPlayer();
	};

	var urlToVideoId = function(url) {
		return url.replace(/^.*?v=([a-zA-Z0-9_\-]+).*$/, '$1');
	};

	var allInputsNode = A.all('#<portlet:namespace />fm input');

	var formNode = A.one('#<portlet:namespace />fm');

	var controlsNode = A.one('#controls');
	var previewNode = A.one('#preview');

	var annotationsNode = A.one('#<portlet:namespace />annotations');
	var borderColorNode = A.one('#<portlet:namespace />borderColor');
	var closedCaptioningNode = A.one('#<portlet:namespace />closedCaptioning');
	var enableEnhancedGenieMenuNode = A.one('#<portlet:namespace />enableEnhancedGenieMenu');
	var enableFullscreenNode = A.one('#<portlet:namespace />enableFullscreen');
	var enableKeyboardControlsNode = A.one('#<portlet:namespace />enableKeyboardControls');
	var enableSearchNode = A.one('#<portlet:namespace />enableSearch');
	var hdNode = A.one('#<portlet:namespace />hd');
	var heightNode = A.one('#<portlet:namespace />height');
	var playerColorNode = A.one('#<portlet:namespace />playerColor');
	var presetSizeNode = A.one('#<portlet:namespace />presetSize');
	var showInfoNode = A.one('#<portlet:namespace />showInfo');
	var showThickerBorderNode = A.one('#<portlet:namespace />showThickerBorder');
	var startTimeNode = A.one('#<portlet:namespace />startTime');
	var urlNode = A.one('#<portlet:namespace />url');
	var widthNode = A.one('#<portlet:namespace />width');

	var imageURL = '<%= imageURL %>';
	var swfURL = '<%= _SWF_URL %>';
	var watchURL = '<%= _WATCH_URL %>';

	A.on(
		'change',
		function(e) {
			createPlayer();
		},
		allInputsNode
	);

	presetSizeNode.on(
		{
			change: presetChange,
			keypress: presetChange
		}
	);

	A.on(
		'change',
		function(e) {
			presetSizeNode.val('');

			presetSizeNode.val(widthNode.val() + 'x' + heightNode.val());
		},
		heightNode
	);

	A.on(
		'change',
		function(e) {
			presetSizeNode.val('');

			presetSizeNode.val(widthNode.val() + 'x' + heightNode.val());
		},
		widthNode
	);

	A.on(
		'click',
		function(e) {
			e.preventDefault();

			submitForm(document['<portlet:namespace />fm']);
		},
		'input.aui-button-input-submit'
	);

	A.on(
		'windowresize',
		function(e) {
			createPlayer();
		}
	);

	new A.ColorPicker(
		{
			after: {
				colorChange: function(e) {
					playerColorNode.val('#' + this.get('hex'));

					createPlayer();
				}
			},
			constrain: true,
			preventOverlap: true,
			triggerParent: playerColorNode.get('parentNode')
		}
	).render();

	new A.ColorPicker(
		{
			after: {
				colorChange: function(e) {
					borderColorNode.val('#' + this.get('hex'));

					createPlayer();
				}
			},
			constrain: true,
			preventOverlap: true,
			triggerParent: borderColorNode.get('parentNode')
		}
	).render();

	if (presetSizeNode.val() == 'custom') {
		A.one('.aui-field.height').removeClass('invisible');
		A.one('.aui-field.width').removeClass('invisible');
	}

	createPlayer();
</aui:script>