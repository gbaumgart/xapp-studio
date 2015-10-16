/opt/local/bin/convert -page +2+2 $1 -matte \
          \( +clone -background gray -shadow 60x2+2+2 \) +swap \
          -background none -mosaic    $2