#!/bin/bash

ZOUT=curl-output.dat

if [ -f $ZOUT ] ; then
    rm $ZOUT
fi

curl --verbose \
     --header "Connection:close" \
     --output $ZOUT \
     http://localhost:8005

echo "# $ZOUT info ..."
ls -l $ZOUT
echo ""

echo "# Testing $ZOUT header ..."
head -1 $ZOUT
echo ""

echo "# Testing $ZOUT footer ..."
tail -1 $ZOUT
echo ""


