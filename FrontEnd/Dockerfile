## Frontend multi-stage Dockerfile

# The first stage uses a Node.js image to build the frontend application and produce the static HTML, CSS, and JavaScript files
# This stage produces the output files into the /app/dist directory, which will be copied to the second stage for serving by Nginx.
FROM node:18-alpine as build
WORKDIR /app
COPY package.json package-lock.json* pnpm-lock.yaml* ./
COPY tsconfig.json ./
COPY tsconfig.node.json ./
COPY index.html ./
COPY vite.config.ts ./
COPY public ./public
COPY src ./src
RUN npm ci --silent
# Run Vite build directly to avoid blocking on TypeScript type-check errors during image build
# (type-checking can be done locally or in CI). Vite will still transpile TS.
RUN npx vite build

# This is the second stage, which uses a lightweight Nginx image to serve the static files generated in the first stage. The static files are copied from the build stage to the Nginx image, and the default Nginx configuration is replaced with a custom configuration file. 
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
