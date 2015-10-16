<div class="home" id="main-content" role="main">
	#if ($browserSniffer.isIe($request) && $browserSniffer.getMajorVersion($request) < 8)
		<table class="portlet-layout">
			<tr>
				<td id="column-1" class="portlet-column portlet-column-only aui-w100 header-column">
					$processor.processColumn("column-1", "portlet-column-content portlet-column-content-only")
				</td>
			</tr>
		</table>
		
		<table class="portlet-layout">
			<tr>
				<td id="column-2" class="aui-w70 portlet-column portlet-column-first">
					$processor.processColumn("column-2", "portlet-column-content portlet-column-content-first")
				</td>
				<td id="column-3" class="aui-w30 portlet-column" id="column-3">
					$processor.processColumn("column-3", "portlet-column-content")
				</td>

			</tr>
		</table>
		<table class="portlet-layout">
			<tr>
				<td id="column-4" class="portlet-column portlet-column-first aui-w100">
					$processor.processColumn("column-5", "portlet-column-content portlet-column-content-first")
				</td>
			</tr>
		</table>
	#else
		<div class="portlet-layout">
			<div id="column-1" class="portlet-column portlet-column-only aui-w100 header-column">
				$processor.processColumn("column-1", "portlet-column-content portlet-column-content-only")
			</div>
		</div>

		<div class="portlet-layout mainStage">
            <div class="mainStageInner">
                <div id="column-3" class="portlet-column aui-w30 right-column" style="min-height:100px;">
                    $processor.processColumn("column-3", "portlet-column-content")
                </div>
                <div id="column-2" class="portlet-column portlet-column-first aui-w70 main-column">
                    $processor.processColumn("column-2", "portlet-column-content portlet-column-content-only")
                </div>
            </div>
		</div>

		<div class="portlet-layout footer-main">
			<div id="column-4" class="portlet-column portlet-column-first aui-w100 bottom-column">
				$processor.processColumn("column-4", "portlet-column-content portlet-column-content-only")
			</div>
		</div>
	#end
</div>