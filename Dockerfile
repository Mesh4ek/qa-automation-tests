FROM maven:3.10.0-amazoncorretto-25-debian-trixie

RUN apt-get update \
    && apt-get install -y --no-install-recommends chromium chromium-driver \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q test -Dgroups=prefetch-only

ENTRYPOINT ["mvn", "-B", \
            "-Dselenide.browserBinary=/usr/bin/chromium", \
            "-Dwebdriver.chrome.driver=/usr/bin/chromedriver", \
            "-Dchromeoptions.args=--no-sandbox,--disable-dev-shm-usage"]
CMD ["test"]