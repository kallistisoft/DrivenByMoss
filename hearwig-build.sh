#!/usr/bin/env bash
mvn clean install package -Dbitwig.extension.directory=target && cp -Rap target/DrivenByMoss.bwextension ..

