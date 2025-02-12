import os
from pathlib import Path

# Constants
BASE_PACKAGE = "wacc"
TEST_DIR = "wacc/Main"
EXAMPLES_DIR = "examples"
CODE_MAP = {"valid": 0, "semanticErr": 200, "syntaxErr": 100}

# Helper function to create unique and consistent class names
def generate_class_name(rel_path):
    parts = rel_path.split(os.sep)
    name_parts = [part.capitalize() for part in parts if part]
    return "".join(name_parts) + "Tests"

# Generate the Scala file content
def generate_scala_content(class_name, dir_path, files, code):
    scala_lines = [
        f"package {BASE_PACKAGE}",
        "",
        "import org.scalatest.flatspec.AnyFlatSpec",
        "import org.scalatest.matchers.should.Matchers.*",
        "",
        f"class {class_name} extends AnyFlatSpec {{",
        f"  val dir = \"src/test/{dir_path}/\"",
        ""
    ]

    for file in files:
        scala_lines.append(
            f"  it should \"pass {file}\" in pending /*{{\n"
            f"    runFrontend(Array(dir+\"{file}\"))._1 shouldBe {code}\n"
            f"  }}*/\n"
        )

    scala_lines.append("}")
    return "\n".join(scala_lines)

# Main script to traverse and generate tests
def main():
    for root, dirs, files in os.walk(EXAMPLES_DIR):
        # Get all .wacc files directly in the current directory (not in subdirectories)
        wacc_files = [f for f in files if f.endswith(".wacc")]
        wacc_files.sort()

        # Skip folders with no .wacc files
        if not wacc_files:
            continue

        # Determine relative path and test type
        rel_path = os.path.relpath(root, EXAMPLES_DIR)
        parts = rel_path.split(os.sep)

        # Determine valid/invalid and specific type (e.g., semanticErr, syntaxErr)
        if parts[0] == "valid":
            test_type = "valid"
            sub_path = os.sep.join(parts[1:])
            scala_dir = os.path.join(TEST_DIR, "valid", os.path.dirname(sub_path))
        elif parts[0] == "invalid":
            if len(parts) > 1 and parts[1] in CODE_MAP:
                test_type = parts[1]  # semanticErr or syntaxErr
                sub_path = os.sep.join(parts[2:])
            else:
                print("Unrecognised type!")
                exit(-1)
            scala_dir = os.path.join(TEST_DIR, "invalid", test_type, os.path.dirname(sub_path))
        else:
            continue  # Skip unrecognized paths

        # Skip if the test type is not recognized
        if test_type not in CODE_MAP:
            continue

        # Generate a unique class name and Scala file name
        class_name = generate_class_name(rel_path)
        scala_file_name = f"{class_name}.scala"
        scala_file_path = os.path.join(scala_dir, scala_file_name)

        # Ensure the output directory exists
        os.makedirs(scala_dir, exist_ok=True)

        # Generate Scala file content
        dir_path = os.path.join(EXAMPLES_DIR, rel_path).replace("\\", "/")
        scala_content = generate_scala_content(class_name, dir_path, wacc_files, CODE_MAP[test_type])

        # Write the Scala file
        with open(scala_file_path, "w") as scala_file:
            scala_file.write(scala_content)
        print(f"Generated: {scala_file_path}")

if __name__ == "__main__":
    main()