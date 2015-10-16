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
            <div>
                <div id="column-3" class="portlet-column portlet-column-first aui-100">
                    $processor.processColumn("column-3", "portlet-column-content portlet-column-content-only")
                </div>
            </div>
		</div>

	#end
</div>