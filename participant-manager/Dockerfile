# Copyright 2020 Google LLC
#
# Use of this source code is governed by an MIT-style
# license that can be found in the LICENSE file or at
# https://opensource.org/licenses/MIT.
# Stage 1
# Installing all the dependencies and creating Dist file or building angular application
FROM node:12.16.1 AS compile-image
ARG basehref=/participant-manager/
WORKDIR /app
COPY . ./
RUN npm set progress=false
RUN npm install
RUN npm install -g @angular/cli
RUN ng build --aot --prod --base-href=$basehref

# Stage2
# copying and running the application in the nginx with the dist file created in stage 1(Angular Build)
FROM nginx
COPY --from=compile-image /app/dist/userRegistrationWeb /usr/share/nginx/html
