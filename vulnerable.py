import os
import sqlite3

# Hardcoded credentials (Weak Security Practice)
USERNAME = "admin"
PASSWORD = "password123"  # 🔴 Trivy should flag this as a hardcoded secret!

# Insecure Command Execution (Command Injection)
user_input = input("Enter a file name: ")
os.system(f"cat {user_input}")  # 🔴 If user inputs `; rm -rf /`, this could delete files!

# Insecure SQL Query (SQL Injection)
conn = sqlite3.connect("test.db")
cursor = conn.cursor()
user_input = input("Enter a username: ")
query = f"SELECT * FROM users WHERE username = '{user_input}'"  # 🔴 Vulnerable to SQL injection!
cursor.execute(query)

# Insecure File Handling
with open("/tmp/test.txt", "w") as f:
    f.write("This is a test file.")  # 🔴 Writing to /tmp can be unsafe if exploited

print("Executed successfully!")
