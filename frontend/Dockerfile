# ============================================
# Frontend Dockerfile (Nginx)
# ============================================
# This Dockerfile uses Nginx to serve the static frontend files.
# It's a lightweight and high-performance web server.

# Use the official Nginx image based on Alpine Linux for a small footprint
FROM nginx:alpine

# Copy all frontend files to the default Nginx public directory
# This includes index.html, dashboard.html, css/, and js/
COPY . /usr/share/nginx/html

# Expose port 80 (standard HTTP port)
EXPOSE 80

# Start Nginx in the foreground
# daemon off; ensures that the container stays running
CMD ["nginx", "-g", "daemon off;"]
