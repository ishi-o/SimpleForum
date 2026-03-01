#!/bin/bash
if [ ! -d "/usr/share/elasticsearch/plugins/analysis-ik" ]; then
	echo "Installing IK Analyzer..."
	./bin/elasticsearch-plugin install --batch https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v8.11.1/elasticsearch-analysis-ik-8.11.1.zip
else
	echo "IK Analyzer already installed"
fi
