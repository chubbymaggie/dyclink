#!/bin/bash

lib1="../colt_graphs"
lib2="../jama_graphs"
lib3="../la4j_graphs"

read -s -p "DB password: " dbpw
echo 'Start executing dyclink'
echo 'Pair1: ' + $lib1 + ' ' + $lib2
java -Xmx60g -cp "bin/:lib/*" edu.columbia.psl.cc.analysis.PageRankSelector $dbpw $lib1 $lib2

echo 'Pair2: ' + $lib1 + ' ' + $lib3
java -Xmx60g -cp "bin/:lib/*" edu.columbia.psl.cc.analysis.PageRankSelector $dbpw $lib1 $lib2

echo 'Pair3: ' + $lib2 + ' ' + $lib3
java -Xmx60g -cp "bin/:lib/*" edu.columbia.psl.cc.analysis.PageRankSelector $dbpw $lib2 $lib3



