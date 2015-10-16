<div class="home" id="main-content" role="main">
	#if ($browserSniffer.isIe($request) && $browserSniffer.getMajorVersion($request) < 8)
		<table class="portlet-layout">
			<tr>
				<td id="column-1" class="portlet-column portlet-column-only aui-w100 header-column">
					$processor.processColumn("column-1", "portlet-column-content portlet-column-content-only")
				</td>
			</tr>
		</table>
	#else


		<div class="portlet-layout">
            <div id="column-1" class="portlet-column portlet-column-only aui-w100 home-header-column">
                $processor.processColumn("column-1", "portlet-column-content portlet-column-content-only home-header-column-inner")
            </div>
        </div>

        <div class="portlet-layout">
            <div class="home-message-inner">
                <div id="column-2" class="portlet-column portlet-column-only aui-w100 home-message-column">
                    $processor.processColumn("column-2", "portlet-column-content portlet-column-content-only")
                </div>
            </div>
        </div>


		<div class="portlet-layout home-main-stage">
            <div class="home-stage-inner">
                <div id="column-3" class="portlet-column portlet-column-first aui-100 home-main-column">
                    $processor.processColumn("column-3", "portlet-column-content portlet-column-content-only")
                </div>
            </div>
		</div>

		<div class="portlet-layout home-main-stage2">
            <div class="home-stage-inner2">
                <div id="column-4" class="portlet-column portlet-column-first aui-100 home-main-column">
                    $processor.processColumn("column-4", "portlet-column-content portlet-column-content-only")
                </div>
            </div>
        </div>

		<div class="portlet-layout footer-main">
            <div id="column-5" class="portlet-column portlet-column-only aui-w100 bottom-column">
                $processor.processColumn("column-5", "portlet-column-content portlet-column-content-only")
            </div>
		</div>


	#end
</div>