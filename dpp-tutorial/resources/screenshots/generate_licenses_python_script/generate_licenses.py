import os

# Define the absolute path to the folder containing the images
image_folder = r".."

# Placeholder content for the .license file
license_template = """Author: Mathias Brunkow Moser
Description: This image was created to support the dpp-tutorial.
License:
- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2024 CGI Deutschland B.V. & Co. KG
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
"""

# Step 1: Delete old .license files
for filename in os.listdir(image_folder):
    # Check if the file is a .license file
    if filename.endswith(".license"):
        file_path = os.path.join(image_folder, filename)
        os.remove(file_path)  # Delete the file
        print(f"Deleted old license file: {file_path}")

# Step 2: Generate new .license files
for filename in os.listdir(image_folder):
    # Check if the file is an image (e.g., .png, .jpg, etc.)
    if filename.endswith((".png", ".jpg", ".jpeg")):
        # Construct the path for the .license file
        license_file = os.path.join(image_folder, f"{filename}.license")
        
        # Write the license template to the .license file
        with open(license_file, "w") as file:
            file.write(license_template)
        print(f"Generated new license file: {license_file}")

print("All old license files deleted and new license files generated!")
