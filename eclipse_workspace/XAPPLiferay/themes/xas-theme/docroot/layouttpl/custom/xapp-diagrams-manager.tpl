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
			<div id="column-1" class="portlet-column portlet-column-only aui-w100 xas-header-column">
				$processor.processColumn("column-1", "portlet-column-content portlet-column-content-only")
			</div>
		</div>

		<div class="portlet-layout xasStage">
            <div class="xapp-diagram-manager-Inner">
                <div id="column-2" class="aui-w100 xapp-diagram-manager-column">
                    $processor.processColumn("column-2", "")
                </div>
            </div>
		</div>

		<div class="portlet-layout">
			<div id="column-3" class="portlet-column aui-w100 xas-bottom-column">
				$processor.processColumn("column-3", "portlet-column-content")
			</div>
		</div>
	#end
</div>