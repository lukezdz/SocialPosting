FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update
RUN apt-get install -y apache2 apache2-utils
RUN apt-get clean
RUN a2enmod headers proxy proxy_http proxy_balancer proxy_hcheck

COPY "./social_posting.conf" "/etc/apache2/sites-available/000-default.conf"

CMD ["apache2ctl", "-D", "FOREGROUND"]

