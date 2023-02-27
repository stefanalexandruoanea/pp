FROM python:3.9-slim-buster
WORKDIR /app
COPY my_file.txt /app
RUN pip install pandas
CMD ["python", "read_file.py"]