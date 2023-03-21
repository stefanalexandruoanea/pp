import tkinter as tk
from tkinter import messagebox
from tkinter import ttk
import math

class Application(tk.Frame):
    
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self.pack()
        self.create_widgets()

    def create_widgets(self):
        self.input_label = tk.Label(self, text="Enter integers separated by spaces:")
        self.input_label.pack()
        self.input_entry = tk.Entry(self)
        self.input_entry.pack()

        self.filter_odd_button = tk.Button(self, text="Filter Odd Numbers", command=self.filter_odd)
        self.filter_odd_button.pack()

        self.filter_prime_button = tk.Button(self, text="Filter Prime Numbers", command=self.filter_prime)
        self.filter_prime_button.pack()

        self.sum_button = tk.Button(self, text="Sum Elements", command=self.sum_elements)
        self.sum_button.pack()

        self.output_label = tk.Label(self, text="Output will be displayed here.")
        self.output_label.pack()

    def filter_odd(self):
        input_str = self.input_entry.get()
        if not input_str:
            messagebox.showerror("Error", "Input cannot be empty.")
            return
        try:
            input_list = [int(x) for x in input_str.split()]
            output_list = [x for x in input_list if x % 2 != 0]
            self.output_label.configure(text=str(output_list))
        except ValueError:
            messagebox.showerror("Error", "Invalid input. Please enter integers separated by spaces.")

    def filter_prime(self):
        input_str = self.input_entry.get()
        if not input_str:
            messagebox.showerror("Error", "Input cannot be empty.")
            return
        try:
            input_list = [int(x) for x in input_str.split()]
            output_list = [x for x in input_list if self.is_prime(x)]
            self.output_label.configure(text=str(output_list))
        except ValueError:
            messagebox.showerror("Error", "Invalid input. Please enter integers separated by spaces.")

    def is_prime(self, n):
        if n < 2:
            return False
        for i in range(2, int(math.sqrt(n)) + 1):
            if n % i == 0:
                return False
        return True

    def sum_elements(self):
        input_str = self.input_entry.get()
        if not input_str:
            messagebox.showerror("Error", "Input cannot be empty.")
            return
        try:
            input_list = [int(x) for x in input_str.split()]
            output = sum(input_list)
            self.output_label.configure(text=str(output))
        except ValueError:
            messagebox.showerror("Error", "Invalid input. Please enter integers separated by spaces.")

root = tk.Tk()
app = Application(master=root)
app.mainloop()
