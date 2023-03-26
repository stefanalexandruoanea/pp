from PyQt5.QtWidgets import QVBoxLayout
import sys
import os
import re
import pika
from PyQt5.QtWidgets import QApplication, QWidget, QFileDialog, QLabel, QPushButton, QLineEdit

class TextToHTMLConverter(QWidget):
    def __init__(self):
        super().__init__()

        self.initUI()

    def initUI(self):
        # Create widgets
        self.fileLabel = QLabel('File:')
        self.fileLineEdit = QLineEdit()
        self.fileButton = QPushButton('Select File')
        self.uploadButton = QPushButton('Upload')

        # Connect signals to slots
        self.fileButton.clicked.connect(self.selectFile)
        self.uploadButton.clicked.connect(self.convertToHTML)

        # Create layout
        layout = QVBoxLayout()
        layout.addWidget(self.fileLabel)
        layout.addWidget(self.fileLineEdit)
        layout.addWidget(self.fileButton)
        layout.addWidget(self.uploadButton)

        # Set layout and window properties
        self.setLayout(layout)
        self.setWindowTitle('Text to HTML Converter')
        self.setGeometry(100, 100, 300, 150)
        self.show()

    def selectFile(self):
        # Open file selector and update text field with selected file path
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        filePath, _ = QFileDialog.getOpenFileName(self,"Select File", "","Text Files (*.txt)", options=options)
        if filePath:
            self.fileLineEdit.setText(filePath)

    def convertToHTML(self):
        # Read text file and convert to HTML
        filePath = self.fileLineEdit.text()
        if not os.path.isfile(filePath):
            return

        with open(filePath, 'r') as file:
            content = file.read()

        # Extract title and paragraphs from text file
        pattern = r'^(.*?)\n(.*?)$'
        match = re.search(pattern, content, re.DOTALL)
        title = match.group(1)
        paragraphs = match.group(2)

        # Convert to HTML format
        html = f'<html><head><title>{title}</title></head><body>'
        for paragraph in paragraphs.split('\n\n'):
            html += f'<p>{paragraph}</p>'
        html += '</body></html>'

        # Send HTML content to C application via message queue
        connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
        channel = connection.channel()
        channel.queue_declare(queue='html_queue')
        channel.basic_publish(exchange='',
                              routing_key='html_queue',
                              body=html)
        connection.close()

        # Commit changes to Git repository
        os.system('git add .')
        os.system('git commit -m "Converted text file to HTML"')

if __name__ == '__main__':
    app = QApplication(sys.argv)
    converter = TextToHTMLConverter()
    sys.exit(app.exec_())
