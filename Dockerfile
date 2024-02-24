FROM ubuntu:latest
LABEL authors="levic"

ENTRYPOINT ["top", "-b"]