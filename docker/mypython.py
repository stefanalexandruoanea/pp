import pandas as pd
import string
def remove_punctuation(text):
    translator = str.maketrans('', '', string.punctuation)
    return text.translate(translator)

df = pd.read_csv('my_file.txt', sep='\t', header=None, names=['Text'])
df['Text'] = df['Text'].apply(remove_punctuation)

print(df)
