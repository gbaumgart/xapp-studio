/opt/local/bin/convert -page +4+4 $1 -matte \
          \( +clone -background gray -shadow 60x4+4+4 \) +swap \
          -background none -mosaic    $2