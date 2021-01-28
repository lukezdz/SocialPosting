FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive 
ARG FINAL_DEST="/var/www/html"

RUN apt-get update
RUN apt-get install -y apache2 apache2-utils
RUN apt-get clean

COPY "./index.html" "${FINAL_DEST}/index.html"
COPY "./index.js" "${FINAL_DEST}/index.js"
COPY "./views" "${FINAL_DEST}/views"
COPY "./dist" "${FINAL_DEST}/dist"
COPY "./css" "${FINAL_DEST}/css"
COPY "./favicon.ico" "${FINAL_DEST}/favicon.ico"

EXPOSE 80

CMD ["apache2ctl", "-D", "FOREGROUND"]