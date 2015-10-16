#!/bin/sh
docsRemote=/mnt/docs/assets
docsLocal=/PMaster/CMAC/shared/SharedAssets/
rsync -a $docsRemote $docsLocal
